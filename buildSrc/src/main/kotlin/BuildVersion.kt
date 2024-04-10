object BuildVersion {
    object environment {
        //region App
        private const val majorVersion = 1
        private const val minorVersion = 0
        private const val bugFixVetsion = 0

        const val minSdkVersion = 23
        const val compileSdkVersion = 34
        const val targetSdkVersion = 34
        const val applicationId = "com.example.poqueapi"
        const val appVersionCode = majorVersion * 1000 + minorVersion * 100 + bugFixVetsion
        const val appVersionName = "${majorVersion}.${minorVersion}.$bugFixVetsion"
        //endregion
    }

}