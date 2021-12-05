//
//  LocationManager.swift
//  iosApp
//
//  Created by Grigory Zenkov on 03.12.2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation
import CoreLocation

class LocationManager: NSObject, ObservableObject {
    
    static let shared = LocationManager()

    private let locationManager = CLLocationManager()
    var location: CLLocation? = nil

    override init() {
        super.init()
        locationManager.delegate = self
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        locationManager.distanceFilter = kCLDistanceFilterNone
        locationManager.requestAlwaysAuthorization()
        locationManager.requestWhenInUseAuthorization()
        locationManager.startUpdatingLocation()
    }

}

extension LocationManager: CLLocationManagerDelegate {
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        guard let location = locations.last else { return }
        self.location = location
    }
    
}


