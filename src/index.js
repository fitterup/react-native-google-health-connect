import moment from 'moment'
import { NativeModules } from 'react-native'

const healthConnect = NativeModules.RNHealthConnectModule

class HealthConnect {
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

  getDailySteps = async (date = moment()) => {
    var offset = moment(date).utcOffset()
    const startDate = moment(date)
      .startOf('day')
      .add(offset, 'minutes')
      .toISOString()
    const endDate = moment().endOf('day').add(offset, 'minutes').toISOString()
    return await healthConnect.getDailySteps(startDate, endDate)
  }
  getDailyHeartRate = async (date = moment()) => {
    var offset = moment(date).utcOffset()
    const startDate = moment(date)
      .startOf('day')
      .add(offset, 'minutes')
      .toISOString()
    const endDate = moment().endOf('day').add(offset, 'minutes').toISOString()
    return await healthConnect.getDailyHeartRate(startDate, endDate)
  }
  getSleepSamples = async (date = moment()) => {
    var offset = moment(date).utcOffset()
    const startDate = moment(date)
      .startOf('day')
      .add(offset, 'minutes')
      .toISOString()
    const endDate = moment().endOf('day').add(offset, 'minutes').toISOString()
    return await healthConnect.getDailySleepSamples(startDate, endDate)
  }
  getAggregatedSteps = async (date = moment()) => {
    var offset = moment(date).utcOffset()
    const startDate = moment(date)
      .startOf('day')
      .add(offset, 'minutes')
      .toISOString()
    const endDate = moment().endOf('day').add(offset, 'minutes').toISOString()
    return await healthConnect.getAggregatedSteps(startDate, endDate)
  }

  getAggregatedDistance = async (date = moment()) => {
    var offset = moment(date).utcOffset()
    const startDate = moment(date)
      .startOf('day')
      .add(offset, 'minutes')
      .toISOString()
    const endDate = moment().endOf('day').add(offset, 'minutes').toISOString()
    return await healthConnect.getAggregatedDistance(startDate, endDate)
  }
}

export default new HealthConnect()
