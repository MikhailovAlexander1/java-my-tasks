(defn vvv [f v1 v2] (mapv f v1 v2))

(defn v+ [v1 v2] (vvv + v1 v2))
(defn v- [v1 v2] (vvv - v1 v2))
(defn v* [v1 v2] (vvv * v1 v2))
(defn vd [v1 v2] (vvv / v1 v2))

(defn scalar [v1 v2] (apply + (v* v1 v2)))

(defn vect [v1 v2]
  (let [result []]
    (conj (conj (conj result (- (* (nth v1 1) (nth v2 2)) (* (nth v2 1) (nth v1 2)))) (- (- (* (nth v1 0) (nth v2 2)) (* (nth v2 0) (nth v1 2))))) (- (* (nth v1 0) (nth v2 1)) (* (nth v2 0) (nth v1 1))))
    ))

(defn vs [v s l]
  (if (< l 0)
    v
    (vs (assoc v l (* (nth v l) s)) s (dec l))
    ))

(defn v*s [v s] (vs v s (dec (count v))))

(defn mmm [f m1 m2 l]
  (if (< l 0)
    m1
    (mmm f (assoc m1 l (vvv f (nth m1 l) (nth m2 l))) m2 (dec l))))

(defn m+ [m1 m2] (mmm + m1 m2 (dec (count m1))))
(defn m- [m1 m2] (mmm - m1 m2 (dec (count m1))))
(defn m* [m1 m2] (mmm * m1 m2 (dec (count m1))))
(defn md [m1 m2] (mmm / m1 m2 (dec (count m1))))

(defn ms [m s l]
  (if (< l 0)
    m
    (ms (assoc m l (v*s (nth m l) s)) s (dec l))))

(defn m*s [m s] (ms m s (dec (count m))))

(defn mv [m v result]
  (if (== (count m) (count result))
    result
    (mv m v (conj result (scalar v (nth m (count result)))))))

(defn m*v [m v] (mv m v []))

(defn transpose [m] (apply mapv vector m))

(defn get-multiply-line [m1 m2 line colon result]
  (if (< colon (count m2))
    (get-multiply-line m1 m2 line (inc colon) (conj result (scalar (nth m1 line) (nth m2 colon))))
    result))

(defn mm [m1 m2 line result]
  (if (< line (count m1))
    (mm m1 m2 (inc line) (conj result (get-multiply-line m1 m2 line 0 [])))
    result))

(defn m*m [m1 m2] (mm m1 (transpose m2) 0 []))

(defn ccc [f c1 c2 l]
  (if (== (count c1) l)
    c1
    (ccc f (assoc c1 l (mmm f (nth c1 l) (nth c2 l) (dec (count (nth c1 l))))) c2 (inc l))))

(defn c+ [c1 c2] (ccc + c1 c2 0))
(defn c- [c1 c2] (ccc - c1 c2 0))
(defn c* [c1 c2] (ccc * c1 c2 0))
(defn cd [c1 c2] (ccc / c1 c2 0))