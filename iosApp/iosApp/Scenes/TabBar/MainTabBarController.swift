//
//  MainTabBarController.swift
//  iosApp
//
//  Created by Grigory Zenkov on 03.12.2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import UIKit
import SwiftUI

class MainTabBarController: UITabBarController, UITabBarControllerDelegate {
    
    @AppStorage("language") var language = LocalizationService.shared.language

    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    override func tabBar(_ tabBar: UITabBar, didSelect item: UITabBarItem) {
        if item.title == "Map" {
            self.navigationController?.title = ""
        }
    }
}


