//
//  HiddenState.swift
//  iosApp
//
//  Created by Grigory Zenkov on 03.12.2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import UIKit

class HiddenState {
    
    static let shared = HiddenState()
    static let changeState = Notification.Name("changestate")
    
    var state: Bool {
        get {
            guard let changeState = UserDefaults.standard.string(forKey: "hide") else {
                return false
            }
            return Bool(changeState) ?? false
        } set {
            if newValue != state {
                UserDefaults.standard.setValue(newValue, forKey: "hide")
                NotificationCenter.default.post(name: HiddenState.changeState, object: nil)
            }
        }
    }
    
}

