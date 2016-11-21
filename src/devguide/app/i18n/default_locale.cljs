(ns app.i18n.default-locale (:require app.i18n.en [untangled.i18n.core :as i18n]))

(reset! i18n/*current-locale* "en")

(swap! i18n/*loaded-translations* #(assoc % :en app.i18n.en/translations))