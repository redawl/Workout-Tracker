// @ts-check
import { defineConfig } from "eslint/config";
import js from "@eslint/js";
import reactRefresh from "eslint-plugin-react-refresh";
import reactHooks from "eslint-plugin-react-hooks";
import tseslint from "typescript-eslint";

export default defineConfig([
  {
    // root: true,
    // env: { browser: true, es2020: true },
    files: ["**/*.{js,ts}"],
    extends: [
      js.configs.recommended,
      reactHooks.configs.flat.recommended,
      tseslint.configs.recommended,
      tseslint.configs.stylistic,
      tseslint.configs.strict,
      // "recommended",
      // "plugin:react/jsx-runtime",
    ],
    ignores: ["dist", ".eslintrc.cjs", "src/workout-api-client"],
    // parser: "@typescript-eslint/parser",
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
