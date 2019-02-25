![Crisp](https://raw.githubusercontent.com/crisp-im/crisp-sdk-android/master/docs/img/logo_blue.png)

Chat with app users, integrate your favorite tools, and deliver a great customer experience.

# Crisp Codename One SDK

This cn1lib adds support for [Crisp](http://crisp.chat/) chat and client API directly from Codename One Applications. The library wraps JavaScript/HTML API directly instead of wrapping the native libraries. The reason for this is that at this time it's exactly what the native libraries are doing so there would be no advantage to wrapping them. However, this approach means that this cn1lib works on the simulator and on all of Codename One's suppoted platforms seamlessly.

This code is based on the source code of Crisps native Android Integration [here](https://github.com/crisp-im/crisp-sdk-android). 

## Usage

The library needs the website id to work and as such you need to wait until the website ID is passed to the underlying API before invoking further API's. From that point on you can use the API as usual. E.g this will place a chat button on the form which will launch crisp:

````java
private void bindCrispToForm(Form f) {
    Crisp c = Crisp.getInstance();
    if(c == null) {
        Crisp.init(WEBSITE_ID, e -> {
            Crisp.getInstance().setEmail(userEmail);
            Crisp.getInstance().bindFab(f);
        });
    } else {
        c.bindFab(f);
    }
}
````

Notice that `init` is required to create the instance of `Crisp` which is a singleton. However, the call to `init` is asynchronous as it creates the webview through which the JavaScript calls are made.

``bindFab` creates a `FloatingActionButton` with a chat icon that launches the Crisp UI. Notice that you can invoke `openChat()` directly to launch that UI manually. 

## Get your website ID

Your website ID can be found in the Crisp App URL:

    https://app.crisp.chat/website/[WEBISTE_ID]/inbox/

Crisp Website ID is an UUID like e30a04ee-f81c-4935-b8d8-5fa55831b1c0

## API's

- `setTokenId(String tokenId)` / `String getTokenId()` - gets/sets the user token

- `setEmail(String email)` - sets the email for the user

- `setNickname(String nickname)` - sets the nickname of the user

- `setPhone(String phone)` - sets the phone number

- `setAvatar(String avatar)` - sets the avatar

- `setData(String key, String value)` - sets data values related to the user

- `setSegments(String segment)` - sets a segment to which the user belongs 

- `reset()` - Clears the user data
    
- `openChat()` - launches the chat UI
    
- `FloatingActionButton chatFab()` - creates a FAB that can launch the chat UI
    
- `bindFab(Form f)` - creates and binds a FAB that can launch the cht UI to the given form
