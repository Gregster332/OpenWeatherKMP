//
//  CurrentWeatherCutomView.swift
//  iosApp
//
//  Created by Grigory Zenkov on 07.12.2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import UIKit
import SwiftUI
import MultiPlatformLibrary
import MultiPlatformLibraryMvvm

class CurrentWeatherCustomView: UIView {
    
    @IBOutlet private weak var cityLabel: UILabel!
    @IBOutlet private weak var descLabel: UILabel!
    @IBOutlet private weak var tempLabel: UILabel!
    
    private var locationManager: LocationManager = LocationManager()
    private var viewModel: SimpleViewModel!
    var city: Welcome!
    var ind = Indicator()
   // @AppStorage("language") var language = LocalizationService.shared.language
    @AppStorage("language") var language = LocalizationService.shared.language

    override func draw(_ rect: CGRect) {
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(goToTheCurrentCityDetailView))
        self.addGestureRecognizer(tapGesture)
        self.tag = 102
        self.layer.cornerRadius = 20
        self.layer.masksToBounds = true
        
        //self.backgroundColor = .red
        viewModel = SimpleViewModel(eventsDispatcher: .init())
        //ind.showIndicator()
        if Reachability.isConnectedToNetwork() {
            viewModel.getCurrentUserLocation(lat: locationManager.location?.coordinate.latitude ?? 0, lon: locationManager.location?.coordinate.longitude ?? 0) { result in
                self.city = result
                if result.name != "Globe" {
                    self.cityLabel.text = "cli".localized(self.language)
                    self.descLabel.text = "\(result.name), \(result.weather.first!.main.lowercased().localized(self.language))"
                    self.tempLabel.text = "\(Int(result.main.temp - 273))ºC"
                    self.setNeedsDisplay()
                } else {
                    self.cityLabel.text = "cli".localized(self.language)
                    self.descLabel.text = "ntr".localized(self.language)
                    self.tempLabel.text = ""
                }
            }
        }
    }
    
    @objc func goToTheCurrentCityDetailView() {
        if city == nil || city.name == "Globe" {
            return
        }
        let sb = UIStoryboard(name: "ViewController", bundle: nil)
        let controller = sb.instantiateViewController(withIdentifier: "DetailViewController") as! DetailViewController
        controller.welcome = viewModel.convertFromWelcomeToRealmClass(welcome: city!)
        self.getCurrentViewController()?.present(controller, animated: true)
        
    }
    
    func getCurrentViewController() -> UIViewController? {
        if let rootController = UIApplication.shared.keyWindow?.rootViewController {
            var currentController: UIViewController! = rootController
            while( currentController.presentedViewController != nil ) {
                currentController = currentController.presentedViewController
            }
            return currentController
        }
        return nil

    }

}
