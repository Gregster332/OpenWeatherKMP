//
//  SettingsViewController.swift
//  iosApp
//
//  Created by Grigory Zenkov on 03.12.2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import UIKit
import SwiftUI

protocol DataBackDelegate: AnyObject {
    func signalToHide(_ variant: Bool)
}

class SettingsViewController: UIViewController {
    
    @IBOutlet private weak var hideSwitch: UISwitch!
    
    weak var delegate: DataBackDelegate? = nil
    @AppStorage("hide") var hide = HiddenState.shared.state

    override func viewDidLoad() {
        super.viewDidLoad()
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        hideSwitch.isOn = hide
    }
    
    @IBAction func goBack() {
        if hideSwitch.isOn {
            HiddenState.shared.state = true
            hide = true
            delegate?.signalToHide(true)
        } else {
            HiddenState.shared.state = false
            hide = false
            delegate?.signalToHide(false)
        }
        self.dismiss(animated: true, completion: nil)
    }

}
