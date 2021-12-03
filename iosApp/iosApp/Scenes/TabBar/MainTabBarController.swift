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
        
        self.tabBar.items?[0].title = "cit".localized(language)
        self.tabBar.items?[1].title = "map".localized(language)
    }
    
//    func tabBarController(_ tabBarController: UITabBarController, didSelect viewController: UIViewController) {
//        let selectedIndex = tabBarController.viewControllers?.firstIndex(of: viewController)!
//        if selectedIndex == 1 {
//            self.navigationController?.title = ""
//            print("h")
//        }
//    }
    
    override func tabBar(_ tabBar: UITabBar, didSelect item: UITabBarItem) {
        if item.title == "Map" {
            self.navigationController?.title = ""
        }
    }
}


