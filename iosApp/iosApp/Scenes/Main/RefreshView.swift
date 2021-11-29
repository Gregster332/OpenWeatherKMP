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
    var button = UIButton()

    init()
    {
        blurImg.frame = UIScreen.main.bounds
        blurImg.backgroundColor = UIColor.black
        blurImg.isUserInteractionEnabled = true
        blurImg.alpha = 0.5
        button.frame = CGRect(x: UIScreen.main.bounds.midX - 100, y:  UIScreen.main.bounds.midY, width: 200, height: 100)
        //button.center = UIScreen.main.bounds
        button.titleLabel?.text = "Need to refresh one more time"
        button.titleLabel?.numberOfLines = 0
        button.titleLabel?.textAlignment = .center
        button.backgroundColor = UIColor(red: 49/255, green: 182/255, blue: 214/255, alpha: 1)
        //let tapGesture = UITapGestureRecognizer(target: self, action: #selector(hideIndicator))
        button.addTarget(self, action: #selector(hideIndicator), for: .allTouchEvents)
    }

    func showIndicator(){
        DispatchQueue.main.async( execute: {

            UIApplication.shared.keyWindow?.addSubview(self.blurImg)
            UIApplication.shared.keyWindow?.addSubview(self.button)
        })
    }
    @objc func hideIndicator(){

        DispatchQueue.main.async( execute:
            {
                self.blurImg.removeFromSuperview()
                self.button.removeFromSuperview()
        })
    }
}
