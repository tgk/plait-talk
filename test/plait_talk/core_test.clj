(ns plait-talk.core-test
  (:require [clojure.test :refer :all]
            [plait-talk.core :refer :all]
            [plait.core :refer [plait]]))

;; domain

(let [currency-a :cupcakes
      currency-b :cupcakes

      unicorn-a (create-unicorn currency-a 10)
      unicorn-b (create-unicorn currency-b 10)

      price 7
      rainbow (create-rainbow unicorn-a price)]
  (book rainbow unicorn-b)
  (balance unicorn-a)) ;; => 17

;; failing version

(deftest book-fail-test
  (let [currency-a :cupcakes
        unicorn-a (create-unicorn currency-a 10)
        price 7
        rainbow (create-rainbow unicorn-a price)]
    (testing "works when unicorn has enough cupcakes"
      (let [currency-b :cupcakes
            unicorn-b (create-unicorn currency-b 10)
            result (book rainbow unicorn-b)]
        (is (= 17 (balance unicorn-a)))
        (is (= 3 (balance unicorn-b)))))
    (testing "fails when currencies differ"
      (let [currency-c :wishes
            unicorn-c (create-unicorn currency-c 10)
            result (book rainbow unicorn-c)]
        ;; (is (= 10 (balance unicorn-a))) ;; fails
        (is (= 10 (balance unicorn-c)))))))

;; let version

(deftest book-let-test
  (testing "works when unicorn has enough cupcakes"
    (let [currency-a :cupcakes
          currency-b :cupcakes

          unicorn-a (create-unicorn currency-a 10)
          unicorn-b (create-unicorn currency-b 10)

          price 7
          rainbow (create-rainbow unicorn-a price)

          result (book rainbow unicorn-b)]
      (is (= 17 (balance unicorn-a)))
      (is (= 3 (balance unicorn-b)))))
  (testing "fails when currencies differ"
    (let [currency-a :wishes
          currency-b :cupcakes

          unicorn-a (create-unicorn currency-a 10)
          unicorn-b (create-unicorn currency-b 10)

          price 7
          rainbow (create-rainbow unicorn-a price)

          result (book rainbow unicorn-b)]
      (is (= 10 (balance unicorn-a)))
      (is (= 10 (balance unicorn-b))))))

;; helper function version

(defn create-test-data
  [& {:keys [currency-a] :or {currency-a :cupcakes}}]
  (let [currency-b :cupcakes

        unicorn-a (create-unicorn currency-a 10)
        unicorn-b (create-unicorn currency-b 10)

        price 7
        rainbow (create-rainbow unicorn-a price)]
    {:unicorn-a unicorn-a
     :unicorn-b unicorn-b
     :rainbow rainbow}))

(deftest book-helper-fn-test
  (testing "works when unicorn has enough cupcakes"
    (let [{:keys [unicorn-a unicorn-b rainbow]} (create-test-data)
          result (book rainbow unicorn-b)]
      (is (= 17 (balance unicorn-a)))
      (is (= 3 (balance unicorn-b)))))
  (testing "fails when currencies differ"
    (let [{:keys [unicorn-a unicorn-b rainbow]} (create-test-data :currency-a :wishes)
          result (book rainbow unicorn-b)]
      (is (= 10 (balance unicorn-a)))
      (is (= 10 (balance unicorn-b))))))

;; plait version

(deftest book-test
  (plait [currency-a :cupcakes
          currency-b :cupcakes

          unicorn-a (create-unicorn currency-a 10)
          unicorn-b (create-unicorn currency-b 10)

          price 7
          rainbow (create-rainbow unicorn-a price)

          result (book rainbow unicorn-b)]
    (testing "works when unicorn has enough cupcakes"
      (is (= 17 (balance unicorn-a)))
      (is (= 3 (balance unicorn-b))))
    (plait [currency-a :wishes]
      (testing "fails when currencies differ"
        (is (= 10 (balance unicorn-a)))
        (is (= 10 (balance unicorn-b)))))))

;;; Exercises

;; Install this example

;; Write test for insufficient funds (should fail)

;; Write helper binding in top-most plait `balance-unaltered?`

;; Introduce age restriction on rainbow rental
