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
    
    
    var welcome: Welcome? = nil
    
    override func viewDidLoad() {
        super.viewDidLoad()

        if welcome != nil {
            cityLabel.text = welcome!.name
            tempLabel.text = "Temperature: \(welcome!.main.temp)"
            DescLabel.text = "Description: \(welcome!.weather[0].main)"
        }
        
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
