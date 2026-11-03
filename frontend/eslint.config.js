// @ts-check
import { defineConfig, globalIgnores } from "eslint/config";
import js from "@eslint/js";
import reactRefresh from "eslint-plugin-react-refresh";
import reactHooks from "eslint-plugin-react-hooks";
import tseslint from "typescript-eslint";

export default defineConfig([
  globalIgnores(["dist", "src/workout-api-client"]),
  {
    files: ["**/*.{tsx,jsx,js,ts}"],
    extends: [
      js.configs.recommended,
      reactHooks.configs.flat.recommended,
      tseslint.configs.recommended,
      tseslint.configs.stylistic,
      tseslint.configs.strict,
    ],
    plugins: {
      "react-refresh": reactRefresh,
      js,
    },
    rules: {
      "react-refresh/only-export-components": [
        "warn",
        { allowConstantExport: true },
      ],
      "react-hooks/rules-of-hooks": "error",
      "react-hooks/exhaustive-deps": "error",
    },
  },
]);
