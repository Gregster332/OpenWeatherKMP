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
    
    @IBOutlet private weak var cityName: UILabel!
    @IBOutlet private weak var descLabel: UILabel!
    @IBOutlet private weak var tempLabel: UILabel!
    @IBOutlet private weak var emoji: UILabel!

    override func awakeFromNib() {
        super.awakeFromNib()
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
    }
    
    func setCellContent(data: RealmCityModel) {
        cityName.text = data.name
        descLabel.text = data.main
        tempLabel.text = "Temperature: \(Int(data.temp - 273))ºC"
        emoji.text = emojis[data.main]
    }
    
}
