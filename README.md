# API Authorization Demo

Use an asymmetric encryption algorithm to sign the parameters when making a request, and have the server verify the signature of the incoming request.

```plantuml
@startuml
title API Request Signing and Verification Process

actor Client
actor Server

rectangle "Client Side (Signing)" {
    Client -> [Prepare Request Parameters]
    [Prepare Request Parameters] -> [Sort & Concatenate Parameters\ninto signString]
    [Sort & Concatenate Parameters\ninto signString] -> [Sign signString with Private Key]
    [Sign signString with Private Key] -> [Generate Base64-encoded Signature]
    [Generate Base64-encoded Signature] -> [Send Request with Parameters + signature]
}

rectangle "Server Side (Verification)" {
    [Send Request with Parameters + signature] -> [Receive Request on Server]
    [Receive Request on Server] -> [Extract Parameters and signature]
    [Extract Parameters and signature] -> [Rebuild signString using\nsame rule (sorted & concatenated)]
    [Rebuild signString using\nsame rule (sorted & concatenated)] -> [Verify signature using Public Key]
    [Verify signature using Public Key] -> [Is Signature Valid?]
}

[Is Signature Valid?] -> [Yes]: Valid
[Yes] -> [Process Business Logic]
[Is Signature Valid?] -> [No]: Invalid
[No] -> [Reject Request (401/403)]

@enduml
```

![plantuml](https://www.plantuml.com/plantuml/svg/fPFVYzD04CVVyrSCzt3qaCCJYmy-w7c7I54aNA0WjW_RtJ6kR7TrTZPQnF_TpNH9Kf16iT3I-J6VcSntSXjP4QUzgzYoGtXLh-01loMC38rjlVKjA6zWZMG_MQtO1Wyr1OqnLfNI70ZkdKNFnMYGEgIg8jIiV9kH5nBlSGPXKQ1N5_2pWlmfmUiNyB4c_Ae8X_wr8hL7HefRIJsN880c4CCbt0Ul5QFFtqdA9hVb037tRvZo20BzrwBIADiJ9tot_1bgibrV_HOF1JsR9h0tw95wpvsA-6nvZLu7WqRMhpWH5jfiNfaDlHas91t7nu5744VghnELZi8LbQPo3rBzFLe6UK2DjXlL2huSXppFkRYKl_x1fFHKP3d4UBgL-RGooovPPwPYf9X_DpxcAg3a41On4AE1Iz3ZBPWhwVD_11b1LdeO1pkMGvrspkh9qSpd2MuLH-5XhPmrBqNHFmQauWF6xVEZfn93lELTXhk-JV_dNMYjtfxXl0yPi_ATW7ghRFWBQXx4N2nldZnUtZpDwxj5RzBU_GO0)