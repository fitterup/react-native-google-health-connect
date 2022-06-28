//
//  RNHealthConnectModule.swift
//  RNHealthConnectModule
//
//  Copyright © 2022 Krupal Patel. All rights reserved.
//

import Foundation

@objc(RNHealthConnectModule)
class RNHealthConnectModule: NSObject {
  @objc
  func constantsToExport() -> [AnyHashable : Any]! {
    return ["count": 1]
  }

  @objc
  static func requiresMainQueueSetup() -> Bool {
    return true
  }
}
