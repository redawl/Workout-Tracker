import { Navigation } from './components/Navigation'
import '@patternfly/react-core/dist/styles/base.css';

import SuperTokens, { SuperTokensWrapper } from "supertokens-auth-react";
import EmailPassword from "supertokens-auth-react/recipe/emailpassword";
import Session from "supertokens-auth-react/recipe/session";
import { EmailPasswordPreBuiltUI } from "supertokens-auth-react/recipe/emailpassword/prebuiltui";
import { canHandleRoute, getRoutingComponent } from "supertokens-auth-react/ui";
import { SessionAuth } from "supertokens-auth-react/recipe/session";
import EmailVerification from "supertokens-auth-react/recipe/emailverification";
import { EmailVerificationPreBuiltUI } from "supertokens-auth-react/recipe/emailverification/prebuiltui";
import { client } from "./workout-api-client/client.gen";

if (import.meta.env.PROD) {
    console.log("Env: PROD")
} else {
    console.log("Env: DEV")
}

client.setConfig({
    baseUrl: '',
})

if (import.meta.env.PROD) {
    // SuperTokens config
    SuperTokens.init({
        appInfo: {
            // learn more about this on https://supertokens.com/docs/thirdpartyemailpassword/appinfo
            appName: "Workout Tracker",
            apiDomain: import.meta.env.VITE_API_DOMAIN,
            websiteDomain: import.meta.env.VITE_API_DOMAIN,
            apiBasePath: "/api/auth",
            websiteBasePath: "/auth",
        },
        recipeList: [
            EmailVerification.init(),
            EmailPassword.init(),
            Session.init(),
        ]
    });
}

function App() {
    if (import.meta.env.DEV) {
        return <Navigation />
    } else {
        if (canHandleRoute([EmailPasswordPreBuiltUI, EmailVerificationPreBuiltUI])) {
            // This renders the login UI on the /auth route
            return getRoutingComponent([EmailPasswordPreBuiltUI, EmailVerificationPreBuiltUI])
        }
        return (
            <SuperTokensWrapper>
                <SessionAuth>
                    <Navigation />
                </SessionAuth>
            </SuperTokensWrapper>
        )
    }
}

export default App
