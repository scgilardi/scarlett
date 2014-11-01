(ns scarlett.core)

(defn assert-ns*
  "Unconditionally throws an AssertionError reporting a failed attempt
  to change *ns* before compiling subsequent forms. See assert-ns."
  [ns-sym]
  (let [message (format "Assert failed: (= (ns-name *ns*) '%s)" ns-sym)]
    (throw (AssertionError. message))))

(defmacro assert-ns
  "If the namespace bound to *ns* at macro expansion time is not named
  ns-sym, expands to a call to assert-ns* that will report the error
  by throwing an AssertionError at run time. Used to detect cases
  where a call to in-ns is innefective at compile time because it was
  not hoisted to the top-level by the compiler and therefore not
  evaluated before the code that follows it.

  Note: macro expanders other than the compiler will include calls to
  assert-ns* in the expansion they produce unless they duplicate the
  compiler's behavior of evaluating forms within top-level do forms
  independently as if they were top level themselves."
  [ns-sym]
  (if-not (= (ns-name *ns*) ns-sym)
    `(assert-ns* '~ns-sym)))

(defmacro ns-declare
  "Declares vars in a specified namespace. As is the case for ns, def,
  and declare forms, ns-declare forms are intended to appear at the
  top level, not nested within other forms. ns-declare forms may
  appear only at the top-level or (after macro expansion) nested
  exclusively within explicit do forms. Improper nesting will trigger
  an assertion failure at run time. Returns nil."
  [ns-sym & var-syms]
  (let [start-ns (ns-name *ns*)]
    `(do
       (in-ns '~ns-sym)
       (assert-ns ~ns-sym)
       (declare ~@var-syms)
       (in-ns '~start-ns)
       nil)))

(defmacro declare+
  "Like clojure.core/declare, but allows namespace-qualified names.
  As is the case for ns, def, and declare forms, declare+ forms are
  intended to appear at the top level, not nested within other forms.
  declare+ forms containing namespace-qualified names may appear only
  at the top-level or (after macro expansion) nested exclusively
  within explicit do forms. Improper nesting will trigger an assertion
  failure at run time. Returns nil. Inspired by:
  http://groups.google.com/d/msg/clojure/pKhiC82funo/eLMiPOpHTNEJ"
  [& names]
  (let [groups (group-by namespace names)
        simple-names (groups nil)
        qualified-groups (dissoc groups nil)
        dequalify (fn [sym] (-> (name sym) (symbol) (with-meta (meta sym))))]
    `(do
       ~@(if simple-names
           [`(declare ~@simple-names)])
       ~@(for [[ns-str qualified-names] qualified-groups]
           `(ns-declare ~(symbol ns-str) ~@(map dequalify qualified-names)))
       nil)))

