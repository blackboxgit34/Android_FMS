package com.humbhi.blackbox.ui.data

import com.humbhi.blackbox.ui.data.db.DBHelper
import com.humbhi.blackbox.ui.data.network.api.ApiHelper

interface DataManager:ApiHelper,DBHelper {
}