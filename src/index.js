import moment from 'moment'
import { NativeModules } from 'react-native'

const healthConnect = NativeModules.RNHealthConnectModule

class RNHealthConnect {
  authorize = (callback) => {
    healthConnect.authorize(
      (result) => {
        callback(result)
      },
      (error) => {
        callback(error)
      }
    )
  }

  checkIsAuthorized = async () => {
    const { isAuthorized } = await healthConnect.isAuthorized()
    return isAuthorized
  }

  // getDailySteps = async (date = moment()) => {
  //   const options = {
  //     startDate: moment(date).startOf('day'),
  //     endDate: moment(date).endOf('day'),
  //   }
  //   return healthConnect.getDailySteps(options)
  // }
}

export default new RNHealthConnect()
