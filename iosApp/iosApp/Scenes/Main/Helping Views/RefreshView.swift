//
//  RefreshView.swift
//  iosApp
//
//  Created by Grigory Zenkov on 27.11.2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import UIKit

public class Indicator {

    public static let sharedInstance = Indicator()
    var blurImg = UIImageView()
    var indicator = UIActivityIndicatorView()

    init() {
        blurImg.frame = UIScreen.main.bounds
        blurImg.backgroundColor = UIColor.black
        blurImg.isUserInteractionEnabled = true
        blurImg.alpha = 0.5
        indicator.style = .whiteLarge
        indicator.center = blurImg.center
        indicator.startAnimating()
        indicator.color = UIColor(red: 49, green: 182, blue: 214, alpha: 1)
    }

    func showIndicator() {
        DispatchQueue.main.async( execute: {
            UIApplication.shared.keyWindow?.addSubview(self.blurImg)
            UIApplication.shared.keyWindow?.addSubview(self.indicator)
        })
    }
    func hideIndicator() {
        DispatchQueue.main.async( execute: {
            self.blurImg.removeFromSuperview()
            self.indicator.removeFromSuperview()
        })
    }
}
