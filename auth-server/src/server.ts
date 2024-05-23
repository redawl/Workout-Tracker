import supertokens from "supertokens-node";
import Session from "supertokens-node/recipe/session";
import Dashboard from "supertokens-node/recipe/dashboard";
import EmailPassword from"supertokens-node/recipe/emailpassword";
import express from 'express';
import cors from 'cors';
import { errorHandler } from "supertokens-node/framework/express";
import { middleware } from "supertokens-node/framework/express";

supertokens.init({
    framework: "express",
    supertokens: {
        // https://try.supertokens.com is for demo purposes. Replace this with the address of your core instance (sign up on supertokens.com), or self host a core.
        connectionURI: "http://supertokens-core:3567",
        // apiKey: <API_KEY(if configured)>,
    },
    appInfo: {
        // learn more about this on https://supertokens.com/docs/emailpassword/appinfo
        appName: "WorkoutTracker",
        apiDomain: "localhost",
        websiteDomain: "localhost",
        apiBasePath: "/api/auth",
        websiteBasePath: "/auth"
    },
    recipeList: [
        EmailPassword.init(),
        Session.init(), // initializes session features
        Dashboard.init() 
    ]
});

let app = express();

app.use(cors({
    origin: "http://localhost",
    allowedHeaders: ["content-type", ...supertokens.getAllCORSHeaders()],
    credentials: true,
}));

// IMPORTANT: CORS should be before the below line.
app.use(middleware());

// Add this AFTER all your routes
app.use(errorHandler())

const port: number = 8000;

app.listen(port, () => {
    console.log(`Auth Server listening on port ${port}`);
})