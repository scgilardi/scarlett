(ns scarlett.core-test
  (:require [clojure.test :refer :all]
            [scarlett.core :refer :all]))

(def this-ns (ns-name *ns*))

(remove-ns 'ns-a)

(deftest test-assert-ns
  (testing "in-ns succeeds when nested only within explicit do's"
    (is (nil? (eval `(do (do (do (in-ns 'ns-a)
                                 (assert-ns ns-a)
                                 nil))))))
    (in-ns this-ns))
  (testing "in-ns fails when not at top-level"
    (is (thrown-with-msg? AssertionError #"ns-name"
                          (eval `(identity (in-ns 'ns-a)
                                           (assert-ns ns-a)))))
    (in-ns this-ns)))

(remove-ns 'ns-b)
(ns-declare ns-b a b c)

(deftest test-ns-declare
  (is (= #{'a 'b 'c} (set (keys (ns-publics 'ns-b)))))
  (testing "ns-declare reports failure when not at top-level"
    (is (thrown-with-msg? AssertionError #"ns-name"
                          (eval `(identity (ns-declare ns-a a)))))
    (in-ns this-ns)))


(remove-ns 'ns-c)
(remove-ns 'ns-d)
(declare+ a b ns-c/a ns-c/b ns-d/a ns-d/b)

(deftest test-declare+
  (is (contains? (ns-publics this-ns) 'a))
  (is (contains? (ns-publics this-ns) 'b))
  (is (= #{'a 'b} (set (keys (ns-publics 'ns-c)))))
  (is (= #{'a 'b} (set (keys (ns-publics 'ns-d)))))
  (testing "declare+ reports failure when not at top-level"
    (is (thrown-with-msg? AssertionError #"ns-name"
                          (eval `(identity (declare+ ns-a/b)))))
    (in-ns this-ns)))
