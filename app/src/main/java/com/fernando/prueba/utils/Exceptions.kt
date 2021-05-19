package com.fernando.prueba.utils

import java.io.IOException

class ApiServiceException(message:String):IOException(message);
class NoInternetException(message: String):IOException(message);