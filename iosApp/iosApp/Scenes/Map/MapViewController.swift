//
//  MapViewController.swift
//  iosApp
//
//  Created by Grigory Zenkov on 03.12.2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import UIKit
import MapKit
import MultiPlatformLibrary
import MultiPlatformLibraryMvvm

class MapViewController: UIViewController {
    
    @IBOutlet private weak var mapView: MKMapView!
    
    var viewModel: SimpleViewModel!
    var cities: [RealmCityModel] = []
    var ind = Indicator()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        viewModel = SimpleViewModel(eventsDispatcher: .init())
        cities = viewModel.realm.fetchAllCities()
        configureMapView()
        for city in 0..<cities.count {
            addCustomPin(cities[city].name,
                         "\(cities[city].main), \(Int(cities[city].temp - 273))ºC",
                         CLLocationCoordinate2D(latitude: cities[city].lat,
                                                longitude: cities[city].lon))
        }
    }
    
    private func configureMapView() {
        mapView.mapType = .satellite
        mapView.showsUserLocation = true
        mapView.showsBuildings = true
        let tapGesture = UILongPressGestureRecognizer(target: self, action: #selector(addNewPin))
        mapView.addGestureRecognizer(tapGesture)
    }
    
    private func addCustomPin(_ title: String, _ subtitle: String, _ coord: CLLocationCoordinate2D) {
        let pin = MKPointAnnotation()
        pin.coordinate = coord
        pin.title = title
        pin.subtitle = subtitle
        DispatchQueue.main.async {
            self.mapView.addAnnotation(pin)
        }
        
    }
    
    @objc private func addNewPin(name: String = "Moscow") {
        ind.showIndicator()
       //coming soon!
    }
    
}

extension MKMapView {
    func centerToLocation(_ location: CLLocation, regionRadius: CLLocationDistance = 1000) {
        let cooedRegion = MKCoordinateRegion(center: location.coordinate, latitudinalMeters: regionRadius, longitudinalMeters: regionRadius)
        setRegion(cooedRegion, animated: true)
    }
}

