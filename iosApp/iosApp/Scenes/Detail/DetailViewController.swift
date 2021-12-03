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
    @IBOutlet private weak var tableView: UITableView!
    
    
    var welcome: RealmCityModel? = nil
    
    override func viewDidLoad() {
        super.viewDidLoad()

        if welcome != nil {
            cityLabel.text = welcome!.name
            tempLabel.text = "Temperature: \(Int(welcome!.temp - 273))ºC"
            DescLabel.text = "Description: \(welcome!.main)"
           configureTableView()
        }
        
        
    }
    
    func configureTableView() {
        tableView.delegate = self
        tableView.dataSource = self
    }
    
    @IBAction func goBack() {
        dismiss(animated: true, completion: nil)
    }
}

extension DetailViewController: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 7
    }
    
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "Cell", for: indexPath)
        
        switch indexPath.row {
        case 0:
            cell.textLabel?.text = "Feels like:"
            cell.detailTextLabel?.text = "\(Int(welcome!.feelsLike - 273))"
        case 1:
            cell.textLabel?.text = "Temp max:"
            cell.detailTextLabel?.text = "\(Int(welcome!.tempMax - 273))"
        case 2:
            cell.textLabel?.text = "Temp min:"
            cell.detailTextLabel?.text = "\(Int(welcome!.tempMin - 273))"
        case 3:
            cell.textLabel?.text = "Pressure:"
            cell.detailTextLabel?.text = "\(welcome!.pressure)hPh"
        case 4:
            cell.textLabel?.text = "Humidity:"
            cell.detailTextLabel?.text = "\(welcome!.humidity)%"
        case 5:
            cell.textLabel?.text = "Susnet:"
            cell.detailTextLabel?.text = "\(Date(timeIntervalSince1970: TimeInterval(welcome!.sunset)).timeIn24HourFormat())"
        case 6:
            cell.textLabel?.text = "Sunrise:"
            cell.detailTextLabel?.text = "\(Date(timeIntervalSince1970: TimeInterval(welcome!.sunrise)).timeIn24HourFormat())"
        default:
            cell.textLabel?.text = ""
            cell.detailTextLabel?.text = ""
        }
        
        return cell
    }
    
    
    
    
}


