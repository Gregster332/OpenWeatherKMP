//
//  CurrentLocationView.swift
//  iosApp
//
//  Created by Grigory Zenkov on 02.12.2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import UIKit
import MultiPlatformLibrary
import MultiPlatformLibraryMvvm
import SwiftUI

class CurrentLocationView: UIView {
    
    @IBOutlet private weak var cityInfo: UILabel!
    @IBOutlet private weak var temperature: UILabel!
    @IBOutlet private weak var mainlabel: UILabel!
    
    private var locationManager: LocationManager = LocationManager()
    private var viewModel: SimpleViewModel!
    var city: Welcome!
    @AppStorage("language") var language = LocalizationService.shared.language
    //var isHide = true
    
    override func draw(_ rect: CGRect) {
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(goToTheCurrentCityDetailView))
        self.addGestureRecognizer(tapGesture)
        self.tag = 102
        viewModel = SimpleViewModel(eventsDispatcher: .init())
        viewModel.getCurrentUserLocation(lat: locationManager.location?.coordinate.latitude ?? 0, lon: locationManager.location?.coordinate.longitude ?? 0) { result in
            self.city = result
            if result.name != "Globe" {
                self.mainlabel.text = "cli".localized(self.language)
                self.cityInfo.text = "\(result.name), \(result.weather.first!.main.lowercased().localized(self.language))"
                self.temperature.text = "\(Int(result.main.temp - 273))ºC"
            } else {
                self.mainlabel.text = "cli".localized(self.language)
                self.cityInfo.text = "ntr".localized(self.language)
                self.temperature.text = ""
            }
        }
    }
    
    @objc func goToTheCurrentCityDetailView() {
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
