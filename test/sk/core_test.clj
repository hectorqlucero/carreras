(ns sk.core-test
  (:require [clojure.test :refer [is testing]]
            [sk.core :refer [a-test deftest]]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 0 1))))
