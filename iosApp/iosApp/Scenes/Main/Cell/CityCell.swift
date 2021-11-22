//
//  CityCell.swift
//  iosApp
//
//  Created by Grigory Zenkov on 19.11.2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import UIKit
import MultiPlatformLibrary

class CityCell: UITableViewCell {
    
    @IBOutlet weak var cityName: UILabel!
    @IBOutlet weak var descLabel: UILabel!
    @IBOutlet weak var tempLabel: UILabel!

    override func awakeFromNib() {
        super.awakeFromNib()
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
    }
    
    func setCellContent(data: Welcome) {
        cityName.text = data.name
        descLabel.text = data.weather[0].main
        tempLabel.text = "Temperature: \(data.main.temp)"
    }
    
}
