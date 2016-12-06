(ns future-app.android.core
  (:require-macros [rum.core :refer [defc]])
  (:require [re-natal.support :as support]
            [rum.core :as rum]))

(set! js/window.React (js/require "react"))
(def ReactNative (js/require "react-native"))

(defn create-element [rn-comp opts & children]
      (apply js/React.createElement rn-comp (clj->js opts) children))

(def app-registry (.-AppRegistry ReactNative))
(def view (partial create-element (.-View ReactNative)))
(def text (partial create-element (.-Text ReactNative)))
(def image (partial create-element (.-Image ReactNative)))
(def touchable-highlight (partial create-element (.-TouchableHighlight ReactNative)))

(def logo-img (js/require "./images/cljs.png"))

(defn alert [title]
      (.alert (.-Alert ReactNative) title))

(defonce app-state (atom {:greeting "Hello Clojure in Android!"}))

(defc AppRoot < rum/cursored-watch [state]
          (view {:style {:flexDirection "column" :margin 40 :alignItems "center"}}
                (text {:style {:fontSize 30 :fontWeight "100" :marginBottom 20 :textAlign "center"}} (:greeting @state))
                (image {:source logo-img
                        :style  {:width 180 :height 180 :marginBottom 30}})
                (touchable-highlight {:style   {:backgroundColor "#f98" :padding 25 :borderRadius 5}
                                      :onPress #(alert "Hello!")}
                                     (text {:style {:color "white" :textAlign "center" :fontWeight "bold"}} "Press me!"))))

(defonce root-component-factory (support/make-root-component-factory))

(defn mount-app [] (support/mount (AppRoot app-state)))

(defn init []
      (mount-app)
      (.registerComponent app-registry "FutureApp" (fn [] root-component-factory)))