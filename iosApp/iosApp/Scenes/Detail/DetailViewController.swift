//
//  DetailViewController.swift
//  iosApp
//
//  Created by Grigory Zenkov on 19.11.2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import UIKit
import MultiPlatformLibrary
import MultiPlatformLibraryMvvm

class DetailViewController: UIViewController {
    
    @IBOutlet private weak var cityLabel: UILabel!
    @IBOutlet private weak var tempLabel: UILabel!
    @IBOutlet private weak var DescLabel: UILabel!
    @IBOutlet private weak var pressure: UILabel!
    @IBOutlet private weak var humidity: UILabel!
    @IBOutlet private weak var sunrise: UILabel!
    @IBOutlet private weak var sunset: UILabel!
    
    var welcome: Welcome? = nil
    
    override func viewDidLoad() {
        super.viewDidLoad()

        if welcome != nil {
            cityLabel.text = welcome!.name
            tempLabel.text = "Temperature: \(welcome!.main.temp)"
            DescLabel.text = "Description: \(welcome!.weather[0].main)"
            pressure.text = "\(welcome!.main.pressure)hPh"
            humidity.text = "\(welcome!.main.humidity)%"
            sunrise.text = "\(Date(timeIntervalSince1970: TimeInterval(welcome!.sys.sunrise)).timeIn24HourFormat())"
            sunset.text = "\(Date(timeIntervalSince1970: TimeInterval(welcome!.sys.sunset)).timeIn24HourFormat())"
        }
        
        
    }
}


