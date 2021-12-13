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
    @IBOutlet private weak var langSegment: UISegmentedControl!
    @IBOutlet private weak var hideView: UIView!
    @IBOutlet private weak var langView: UIView!
    @IBOutlet private weak var backButton: UIButton!
    @IBOutlet private weak var hideLabel: UILabel!
    @IBOutlet private weak var langLabel: UILabel!
    
    weak var delegate: DataBackDelegate? = nil
    @AppStorage("hide") var hide = HiddenState.shared.state
    @AppStorage("language") var language = LocalizationService.shared.language

    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        backButton.setTitle("gb".localized(language), for: .normal)
        hideLabel.text = "hdl".localized(language)
        langLabel.text = "cl".localized(language)
        hideSwitch.isOn = hide
        langSegment.selectedSegmentIndex = language == .english_us ? 0 : 1
        hideView.layer.cornerRadius = 15
        hideView.dropShadow(scale: false)
        langView.layer.cornerRadius = 15
        langView.dropShadow(scale: false)
    }
    
    @IBAction func changeLanguage() {
        switch langSegment.selectedSegmentIndex {
        case 0:
            LocalizationService.shared.language = .english_us
            language = .english_us
            backButton.setTitle("gb".localized(language), for: .normal)
            hideLabel.text = "hdl".localized(language)
            langLabel.text = "cl".localized(language)
        case 1:
            LocalizationService.shared.language = .russian
            language = .russian
            backButton.setTitle("gb".localized(language), for: .normal)
            hideLabel.text = "hdl".localized(language)
            langLabel.text = "cl".localized(language)
        default: return
        }
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


extension UIView {
    func dropShadow(scale: Bool = true) {
        layer.masksToBounds = false
        layer.shadowColor = UIColor.black.cgColor
        layer.shadowOpacity = 0.2
        layer.shadowOffset = CGSize(width: 1, height: 1)
        layer.shadowRadius = 20
        layer.shadowPath = UIBezierPath(rect: bounds).cgPath
      }
}
