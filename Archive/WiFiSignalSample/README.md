WifiSignal Sample App
=======

This app tests the feasibility of using WIFI signal strength for indoor localization. The underlying principle is the wifi signal strength varies across different location. The app retrieve a list of wifi signal and bind them to a current location. After sampling enough locations to form a database, we do the localization by comparing the signal strength of current location to the record on the database. The most similar one is chosen as the location.
