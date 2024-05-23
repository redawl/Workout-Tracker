import { Navigation } from './components/Navigation'
import '@patternfly/react-core/dist/styles/base.css';

import SuperTokens, { SuperTokensWrapper } from "supertokens-auth-react";
import EmailPassword from "supertokens-auth-react/recipe/emailpassword";
import Session from "supertokens-auth-react/recipe/session";
import { EmailPasswordPreBuiltUI } from "supertokens-auth-react/recipe/emailpassword/prebuiltui";
import { canHandleRoute, getRoutingComponent } from "supertokens-auth-react/ui";
import { SessionAuth } from "supertokens-auth-react/recipe/session";

SuperTokens.init({
    appInfo: {
        // learn more about this on https://supertokens.com/docs/thirdpartyemailpassword/appinfo
        appName: "Workout Tracker",
        apiDomain: "127.0.0.1",
        websiteDomain: "127.0.0.1",
        apiBasePath: "/api/auth",
        websiteBasePath: "/auth",
    },
    recipeList: [
        EmailPassword.init(),
        Session.init(),
    ]
});

function App() {
    if (canHandleRoute([EmailPasswordPreBuiltUI])) {
        // This renders the login UI on the /auth route
        return getRoutingComponent([EmailPasswordPreBuiltUI])
    }

    return (
        <SuperTokensWrapper>
            <SessionAuth>
                <Navigation />
            </SessionAuth>
        </SuperTokensWrapper>
    )
}

export default App
