(defn constant [value] (fn [& args] value))

(defn variable [letter] (fn [m] (get m letter)))
(defn action [f a b] (fn [m] (f (a m) (b m))))

(defn add [a b] (action + a b))
(defn subtract [a b] (action - a b))
(defn multiply [a b] (action * a b))
(defn divide [a b]
  (fn [m] (/ (double (a m)) (double (b m)))))
(defn pow [a b] (fn [m] (Math/pow (a m) (b m))))
(defn log [a b] (fn [m] (/ (Math/log (Math/abs (b m))) (Math/log (Math/abs (a m))))))

(defn negate [a] (fn [m] (- (a m))))

(def bin-ops {'- subtract '+ add '* multiply '/ divide 'pow pow 'log log})
(def unary-ops {negate negate})
(def vs {'x (variable "x") 'y (variable "y") 'z (variable "z")})

(defn parse [l]
  (cond
    (list? l) (if (contains? bin-ops (first l))
                ((get bin-ops (first l)) (parse (second l)) (parse (last l)))
                (negate (parse (second l))))
    (number? l) (constant l)
    (symbol? l) (get vs l)
    )
  )
(defn parseFunction [l] (parse (read-string l)))

;______________________________________________________________________________
;OBJECT_EXPRESSIONS
;______________________________________________________________________________
(declare Constant)
(declare Add)
(declare Subtract)
(declare Multiply)
(declare Divide)
(declare Negate)
(declare Ln)
(declare Pow)
(def for-constant {:x 0
                   :toString (fn [this] (str (:x this)))
                   :evaluate (fn [this m] (:x this))
                   :diff (fn [& args] (Constant 0))
                   })
(defn Constant [x] (assoc for-constant :x x))

(def for-variable {:x ""
                   :toString (fn [this] (:x this))
                   :evaluate (fn [this m] (get m (:x this)))
                   :diff (fn [this d] (cond
                                        (= d (:x this)) (Constant 1)
                                        :else (Constant 0)))
                   })
(defn Variable [x] (assoc for-variable :x x))

(defn evaluate [expr m] ((:evaluate expr) expr m))
(defn toString [expr] ((:toString expr) expr))
(defn diff [expr d] ((:diff expr) expr d))
(defn get-result-for-bins [this m] ((:sign this)
                                    (evaluate (:a this) m)
                                    (evaluate (:b this) m)))
(defn get-result-for-divide [this m] (/ (double (evaluate (:a this) m))
                                        (double (evaluate (:b this) m))))
(defn get-result-for-pow [this m] (Math/pow (evaluate (:a this) m)
                                            (evaluate (:b this) m)))
(defn get-result-for-log [this m] (/ (Math/log (Math/abs (evaluate (:b this) m)))
                                     (Math/log (Math/abs (evaluate (:a this) m)))))
(defn get-diff-for-multiply [this d] (Add (Multiply (diff (:a this) d) (:b this)) (Multiply (:a this) (diff (:b this) d))))
(defn get-diff-for-divide [this d] (Divide (Subtract (Multiply (diff (:a this) d) (:b this)) (Multiply (:a this) (diff (:b this) d)))
                                           (Multiply (:b this) (:b this))))
(defn diff-for-bins-support [this d case] (cond
                                            (= case 1) (Add (diff (:a this) d) (diff (:b this) d))
                                            :else (Subtract (diff (:a this) d) (diff (:b this) d))))
(defn get-diff-for-bins-ops [this d] (cond
                                       (= (:sign this) +) (diff-for-bins-support this d 1)
                                       :else (diff-for-bins-support this d 2)))
(defn get-diff-for-log [this d] (diff (Divide (Ln (:b this)) (Ln (:a this))) d))
(defn get-diff-for-pow [this d] (Multiply (Pow (:a this) (:b this)) (diff (Multiply (Ln (:a this)) (:b this)) d)))

(def signs-for-out {+ '+ - '- * '* / '/ "pow" "pow" "log" "log"})
(def for-bin-ops {:a 0
                  :b 0
                  :sign -
                  :toString (fn [this] (str "(" (get signs-for-out (:sign this)) " "
                                            ((:toString (:a this)) (:a this)) " "
                                            ((:toString (:b this)) (:b this)) ")"))
                  :evaluate (fn [this m] (cond
                                           (= (:sign this) "pow") (get-result-for-pow this m)
                                           (= (:sign this) "log") (get-result-for-log this m)
                                           (= (:sign this) /) (get-result-for-divide this m)
                                           :else (get-result-for-bins this m)))
                  :diff (fn [this d] (cond
                                       (= (:sign this) "pow") (get-diff-for-pow this d)
                                       (= (:sign this) "log") (get-diff-for-log this d)
                                       (= (:sign this) *) (get-diff-for-multiply this d)
                                       (= (:sign this) /) (get-diff-for-divide this d)
                                       :else (get-diff-for-bins-ops this d)
                                       ))
                  })
(defn get-result-for-negate [this m] (- (evaluate (:a this) m)))
(defn get-result-for-ln [this m] (Math/log (Math/abs (evaluate (:a this) m))))
(defn get-diff-for-negate [this d] (Negate (diff (:a this) d)))
(defn get-diff-for-ln [this d] (Divide (diff (:a this) d) (:a this)))
(def for-unary-ops {:a 0
                    :sign ""
                    :toString (fn [this] (str "(" (:sign this) " "
                                              ((:toString (:a this)) (:a this)) ")"))
                    :evaluate (fn [this m] (cond
                                             (= (:sign this) "negate") (get-result-for-negate this m)
                                             :else (get-result-for-ln this m)
                                             ))
                    :diff (fn [this d] (cond
                                         (= (:sign this) "negate") (get-diff-for-negate this d)
                                         :else (get-diff-for-ln this d)))
                    })
(defn constructor [this sign a & args] (cond
                                         (= this for-bin-ops) (assoc this :sign sign :a a :b (nth args 0))
                                         :else (assoc this :sign sign :a a)))

(defn Add [a b] (constructor for-bin-ops + a b))
(defn Subtract [a b] (constructor for-bin-ops - a b))
(defn Multiply [a b] (constructor for-bin-ops * a b))
(defn Divide [a b] (constructor for-bin-ops / a b))
(defn Pow [a b] (constructor for-bin-ops "pow" a b))
(defn Log [a b] (constructor for-bin-ops "log" a b))
(defn Negate [a] (constructor for-unary-ops "negate" a))
(defn Ln [a] (constructor for-unary-ops "ln" a))

(def for-vars-object-parse {'x (Variable "x") 'y (Variable "y") 'z (Variable "z")})
(def for-bin-object-parse {'+ Add '- Subtract '* Multiply '/ Divide 'pow Pow 'log Log})

(defn parse-object-support [l]
  (cond
    (list? l) (if (contains? for-bin-object-parse (first l))
                ((get for-bin-object-parse (first l)) (parse-object-support (second l)) (parse-object-support (last l)))
                (Negate (parse-object-support (second l))))
    (number? l) (Constant l)
    (symbol? l) (get for-vars-object-parse l)
    )
  )

(defn parseObject [l] (parse-object-support (read-string l)))