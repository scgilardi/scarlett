(ns scarlett.core-test
  (:require [clojure.test :refer :all]
            [scarlett.core :refer :all]))

(def this-ns (ns-name *ns*))

(doseq [ns ['ns-a 'ns-b 'ns-c 'ns-d]]
  (remove-ns ns))

(defn read-eval
  "helper to allow compiling at top-level from within tests"
  [s]
  (in-ns this-ns)
  (eval (read-string s)))

(deftest test-assert-ns
  ;; "in-ns succeeds" here means that the in-ns form changed *ns* before
  ;; the form after it was *compiled*
  ;; The tests demonstrate that assert-ns properly reports when in-ns
  ;; succeeds and when it fails
  (testing "in-ns succeeds when nested only within explicit do's"
    (is (= :good (read-eval "(do (do (do (in-ns 'ns-a)
                                         (scarlett.core/assert-ns ns-a)
                                         :good)))"))))
  (testing "in-ns fails when it's not do's all the way up"
    (is (thrown-with-msg? AssertionError #"ns-name"
                          (read-eval "(do (when true
                                            (in-ns 'ns-a)
                                            (scarlett.core/assert-ns ns-a)
                                            :good))")))))

(deftest test-ns-declare
  (testing "ns-declare succeeds when at the top level"
    (is nil? (read-eval "(ns-declare ns-b a b c)"))
    (is (= #{'a 'b 'c} (set (keys (ns-publics 'ns-b))))))
  (testing "ns-declare reports failure when not at top-level"
    (is (thrown-with-msg? AssertionError #"ns-name"
                          (read-eval "(when true (ns-declare ns-a a))")))
    (is (empty? (ns-publics 'ns-a)))))

(deftest test-declare+
  (testing "declare+ succeeds when at the top level"
    (is nil? (read-eval "(declare+ a b ns-c/a ns-c/b ns-d/a ns-d/b ns-d/c)"))
    (is (contains? (ns-publics this-ns) 'a))
    (is (contains? (ns-publics this-ns) 'b))
    (is (= #{'a 'b} (set (keys (ns-publics 'ns-c)))))
    (is (= #{'a 'b 'c} (set (keys (ns-publics 'ns-d))))))
  (testing "declare+ reports failure when not at top-level"
    (is (thrown-with-msg? AssertionError #"ns-name"
                          (read-eval "(when true (declare+ ns-a/b))")))
    (is (empty? (ns-publics 'ns-a)))))
