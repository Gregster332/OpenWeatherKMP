//
//  Date+Extension.swift
//  iosApp
//
//  Created by Grigory Zenkov on 22.11.2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation

extension Date {
    func timeIn24HourFormat() -> String {
           let formatter = DateFormatter()
           formatter.timeZone = TimeZone.current
           formatter.dateStyle = .none
           formatter.dateFormat = "HH:mm"
           return formatter.string(from: self)
       }
}
