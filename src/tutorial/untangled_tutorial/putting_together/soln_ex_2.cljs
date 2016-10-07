(ns untangled-tutorial.putting-together.soln-ex-2
  (:require-macros [cljs.test :refer [is]]
                   [untangled-tutorial.tutmacros :refer [untangled-app]])
  (:require [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]
            [devcards.core :as dc :refer-macros [defcard defcard-doc]]
            [untangled.client.core :as uc]
            [untangled.client.data-fetch :as df]
            [untangled.client.mutations :as m]))

(defmethod m/mutate 'pit-soln/toggle-done [{:keys [state]} k {:keys [id]}]
  {:action (fn [] (swap! state update-in [:items/by-id id :item/done] not))})

(defmethod m/mutate 'pit-soln/delete-item [{:keys [state]} k {:keys [list id]}]
  {:action (fn []
             (let [current-items (get-in @state [:lists/by-title list :list/items])
                   ident-to-remove [:items/by-id id]
                   result-items (vec (filter #(not= ident-to-remove %) current-items))]
               (swap! state (fn [m]
                              (-> m
                                  (assoc-in [:lists/by-title list :list/items] result-items)
                                  (update-in [:items/by-id] dissoc id))))))})

(defmethod m/mutate 'pit-soln/add-item [{:keys [state]} k {:keys [tempid label list]}]
  {:action (fn []
             (let [new-ident [:items/by-id tempid]
                   new-item {:item/id tempid :item/label label :item/done false}]
               (swap! state assoc-in [:items/by-id tempid] new-item)
               (uc/integrate-ident! state new-ident :append [:lists/by-title list :list/items])))})

(defui ^:once TodoItem
  static uc/InitialAppState
  (initial-state [this {:keys [id label complete]}] {:item/id id :item/label label :item/done complete})
  static om/IQuery
  (query [this] [:item/id :item/label :item/done])
  static om/Ident
  (ident [this props] [:items/by-id (:item/id props)])
  Object
  (render [this]
    (let [{:keys [item/id item/label item/done]} (om/props this)
          delete (om/get-computed this :onDelete)]
      (dom/li nil
              (dom/input #js {:type "checkbox" :onChange #(om/transact! this `[(pit-soln/toggle-done ~{:id id})]) :checked done})
              label
              (dom/button #js {:onClick #(when delete (delete id))} "X")))))

(def ui-item (om/factory TodoItem))

(defui ^:once ItemList
  static uc/InitialAppState
  (initial-state [this params] {:list/title "My List" :list/items [(uc/initial-state TodoItem {:id 1 :label "A" :complete false})
                                                                   (uc/initial-state TodoItem {:id 2 :label "B" :complete false})
                                                                   (uc/initial-state TodoItem {:id 3 :label "C" :complete true})
                                                                   (uc/initial-state TodoItem {:id 4 :label "D" :complete false})]})
  static om/IQuery
  (query [this] [:ui/new-item-text :list/title {:list/items (om/get-query TodoItem)}])
  static om/Ident
  (ident [this props] [:lists/by-title (:list/title props)])
  Object
  (render [this]
    (let [{:keys [ui/new-item-text list/title list/items] :or {ui/new-item-text ""}} (om/props this)
          delete-item (fn [item-id] (om/transact! this `[(pit-soln/delete-item {:list ~title :id ~item-id})]))]
      (dom/div nil
        (dom/h4 nil title)
        (dom/input #js {:value new-item-text :onChange (fn [evt] (m/set-string! this :ui/new-item-text :event evt))})
        (dom/button #js {:onClick #(om/transact! this `[(pit-soln/add-item {:tempid ~(om/tempid)
                                                                            :label  ~new-item-text
                                                                            :list   ~title})])} "Add")
        (dom/ol nil (map (fn [item] (ui-item (om/computed item {:onDelete delete-item}))) items))))))

(def ui-item-list (om/factory ItemList))

(defui ^:once TodoList
  static uc/InitialAppState
  (initial-state [this params] {:ui/react-key "A" :item-list (uc/initial-state ItemList nil)})
  static om/IQuery
  (query [this] [:ui/react-key {:item-list (om/get-query ItemList)}])
  Object
  (render [this]
    (let [{:keys [ui/react-key item-list]} (om/props this)]
      (dom/div #js {:key react-key}
        (ui-item-list item-list)))))
