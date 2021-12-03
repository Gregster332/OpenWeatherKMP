//
//  CityAnnotation.swift
//  iosApp
//
//  Created by Grigory Zenkov on 03.12.2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation
import MapKit

class CityAnnotation: NSObject, MKAnnotation {
    var coordinate: CLLocationCoordinate2D
    let title: String?
    let temp: Int?
    let desc: String?
    
    init(title: String, temp: Int, desc: String, coordinate: CLLocationCoordinate2D) {
        self.title = title
        self.temp = temp
        self.desc = desc
        self.coordinate = coordinate
        
        super.init()
    }
}
