(ns untangled-devguide.guide
  (:require
    untangled-devguide.A-Introduction
    untangled-devguide.A-Quick-Tour
    untangled-devguide.B-UI
    untangled-devguide.B-UI-Exercises
    untangled-devguide.C-App-Database
    untangled-devguide.C-App-Database-Exercises
    untangled-devguide.D-Queries
    untangled-devguide.E-UI-Queries-and-State
    untangled-devguide.E-UI-Queries-and-State-Exercises
    untangled-devguide.F-Untangled-Client
    untangled-devguide.F-Untangled-DevEnv
    untangled-devguide.F-Untangled-Initial-App-State
    untangled-devguide.G-Mutation
    untangled-devguide.G-Mutation-Exercises
    untangled-devguide.G-Mutation-Solutions
    untangled-devguide.H-Server-Interactions
    untangled-devguide.I-Building-A-Server
    untangled-devguide.I-Building-A-Server-Exercises
    untangled-devguide.J-Putting-It-Together
    untangled-devguide.K-Testing
    untangled-devguide.L-Internationalization
    untangled-devguide.M10-Advanced-UI
    untangled-devguide.M30-Advanced-Mutation
    untangled-devguide.M40-Advanced-Server-Topics
    untangled-devguide.Z-Deploying-To-Heroku
    untangled-devguide.Z-Further-Reading
    untangled-devguide.Z-Glossary
    untangled-devguide.Z-Query-Quoting
    app.i18n.default-locale
    [devtools.core :as devtools]))

(defonce devtools-installed (devtools/install!))
