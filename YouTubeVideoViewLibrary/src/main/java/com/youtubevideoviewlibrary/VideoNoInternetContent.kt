package com.youtubevideoviewlibrary


object VideoNoInternetContent {
    fun compose():String{
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Youtube Video Internet Error</title>
                <style>
                    html,body{
                        width:100%;
                        height:100%;
                        left:0;
                        top:0;
                        position:absolute;
                        background:white;
                        margin:0;
                        padding:0;
                        white-space: nowrap;
                    }
                    #container{
                        border: 1px solid #f0f0f0;
                        width:calc(100% - 4px);
                        height:calc(100% - 4px);
                        left:0;
                        top:0;
                        position:absolute;
                        margin:0;
                        padding:0;
                        border-radius:5px;
                        text-align:center;
                    }
                    .parent {
                      display: flex;
                      flex-flow: column;
                    }
                    .parent_row {
                      display: flex;
                      flex-flow: row;
                    }
                    .wrap_content {
                      flex: 0 1 auto;
                    }
                    .match_parent {
                      flex: 1 1 auto;
                    }
                    .filler{
                        width:100%;
                        height:100%;
                    }
                </style>
            </head>
            <body>

            <div id="container" class="parent">
                <div class="match_parent filler"></div>
                <div class="wrap_content parent_row" style="width:100%">
                    <div class="match_parent filler"></div>
                    <div class="wrap_content">
                        <svg width="24px" height="24px" viewBox="0 0 24 24" role="img" xmlns="http://www.w3.org/2000/svg" aria-labelledby="videoIconTitle" stroke="#000000" stroke-width="1" stroke-linecap="square" stroke-linejoin="miter" fill="none" color="#000000"> <title id="videoIconTitle">Video</title> <polygon points="18 12 9 16.9 9 7"/> <circle cx="12" cy="12" r="10"/> </svg>
                        <p>No Internet</p>
                    </div>
                    <div class="match_parent filler"></div>
                </div>
                <div class="match_parent filler"></div>
            </div>

            </body>
            </html>
        """.trimIndent()
    }
}