import * as React from 'react'
import { NativeModules } from 'react-native'

const healthConnect = NativeModules.RNHealthConnectModule

class RNHealthConnect {
  authorize = () => {
    healthConnect.authorize()
  }
}

export default new RNHealthConnect()
