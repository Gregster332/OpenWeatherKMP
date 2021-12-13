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
import SwiftUI
import Network

class MapViewController: UIViewController {
    
    @IBOutlet private weak var mapView: MKMapView!
    @IBOutlet private weak var imageView: UIImageView!
    
    @AppStorage("language") var language = LocalizationService.shared.language
    
    var cities: [RealmCityModel] = []
    var ind = Indicator()
    var viewModel = SimpleViewModel(eventsDispatcher: .init())
    
    override func viewDidLoad() {
        super.viewDidLoad()
        monitorInternetConnection()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        if Reachability.isConnectedToNetwork() {
        removeAllAnnotations()
        cities = viewModel.realm.fetchAllCities()
        configureMapView()
        for city in 0..<cities.count {
            addCustomPin(cities[city].name,
                         "\(cities[city].main.lowercased().localized(language)), \(Int(cities[city].temp - 273))ºC",
                         CLLocationCoordinate2D(latitude: cities[city].lat,
                                                longitude: cities[city].lon), delete: false)
        }
        } else {
            mapView.isHidden = true
        }
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        tabBarItem.title = "map".localized(language)
    }
    
    private func removeAllAnnotations() {
        self.mapView.removeAnnotations(self.mapView.annotations)
    }
    
    private func configureMapView() {
        mapView.mapType = .satellite
        mapView.showsUserLocation = true
        mapView.showsBuildings = true
    }
    
    private func addCustomPin(_ title: String, _ subtitle: String, _ coord: CLLocationCoordinate2D, delete: Bool) {
        let pin = MKPointAnnotation()
        pin.coordinate = coord
        pin.title = title
        pin.subtitle = subtitle
        DispatchQueue.main.async {
            if !delete {
                self.mapView.addAnnotation(pin)
            } else {
                self.mapView.removeAnnotation(pin)
            }
        }
    }
    
    @objc private func addNewPin(name: String = "Moscow") {
        ind.showIndicator()
       //coming soon!
    }
    
    func monitorInternetConnection() {
        let monitor = NWPathMonitor()
        monitor.pathUpdateHandler = { path in
            if path.status == .satisfied {
                    DispatchQueue.main.async {
                        self.mapView.isHidden = false
                    }
            } else {
                DispatchQueue.main.async {
                    self.mapView.isHidden = true
                }
            }
        }
        
        let queue = DispatchQueue(label: "Network")
        monitor.start(queue: queue)
    }
    
}

extension MKMapView {
    func centerToLocation(_ location: CLLocation, regionRadius: CLLocationDistance = 1000) {
        let cooedRegion = MKCoordinateRegion(center: location.coordinate, latitudinalMeters: regionRadius, longitudinalMeters: regionRadius)
        setRegion(cooedRegion, animated: true)
    }
}

