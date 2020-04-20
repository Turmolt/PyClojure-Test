(ns pytest.core
  (:require [libpython-clj.require :refer [require-python]]
            [libpython-clj.python :as py :refer [py. py.. py.-]]
            [clojure.java.shell :as sh]))

(def mplt (py/import-module "matplotlib"))
(py. mplt "use" "Agg")

(require-python '[matplotlib.pyplot :as pyplot])
(require-python 'matplotlib.backends.backend_agg)
(require-python 'numpy)

(defmacro with-show
  [& body]
  `(let [_# (pyplot/clf)
         fig# (pyplot/figure)
         agg-canvas# (matplotlib.backends.backend_agg/FigureCanvasAgg fig#)]
     ~(cons 'do body)
     (py. agg-canvas# "draw")
     (pyplot/savefig "temp.png")
     (sh/sh "open" "temp.png")))

(def x (numpy/linspace 0 2 50))


(let [labels ["Cats" "Dogs" "Horses" "Chickens"]
      sizes [6 7 10 6]
      explode [0 0.1 0 0]]
  (with-show 
    (let [[fig1 ax1] (pyplot/subplots)]
      (py. ax1 "pie" sizes 
           :explode explode 
           :labels labels 
           :autopct "%1.1f%%"
           :shadow true
           :startangle 90)
      (py. ax1 "axis" "equal")
      (pyplot/title "Animals"))))
