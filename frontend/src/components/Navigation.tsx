import { useEffect, useState } from 'react';
import {
    Page,
    Masthead,
    MastheadToggle,
    MastheadMain,
    MastheadLogo, MastheadBrand,
    MastheadContent,
    PageSidebar,
    PageSidebarBody,
    PageSection,
    PageToggleButton,
    Toolbar,
    ToolbarContent,
    ToolbarItem,
    NavItem,
    Nav,
    Button,
    ToggleGroup,
    ToggleGroupItem,
    ToolbarGroup,
} from '@patternfly/react-core';
import { GithubIcon, SunIcon, MoonIcon } from '@patternfly/react-icons';
import { WORKOUTS, EXERCISES, REPORTS, DARK, LIGHT, DOCUMENTATION } from './constants';
import { signOut } from "supertokens-auth-react/recipe/emailpassword";
import { Workouts } from '../pages/Workouts.tsx';
import { Exercises } from '../pages/Exercises.tsx';
import { Reports } from '../pages/Reports.tsx';
import { Documentation } from '../pages/Documentation.tsx';

export const Navigation = () => {
    const [isSidebarOpen, setIsSidebarOpen] = useState(true);
    const [currentPage, setCurrentPage] = useState(WORKOUTS as string | number);
    const [theme, setTheme] = useState(DARK);

    async function logOut() {
        await signOut();
        window.location.href = '/auth';
    }

    useEffect(() => {
        const savedTheme = localStorage.getItem('theme');
        const html = document.getElementsByTagName('html')[0];

        if (savedTheme === DARK) {
            html.className = 'pf-v6-theme-glass pf-v6-theme-dark';
            setTheme(DARK);
            localStorage.setItem('theme', DARK);
        } else {
            html.className = 'pf-v6-theme-glass';
            setTheme(LIGHT);
            localStorage.setItem('theme', LIGHT);
        }
    }, [theme])

    const onSidebarToggle = () => {
        setIsSidebarOpen(!isSidebarOpen);
    };

    const toggleDarkMode = ((currentTarget: string, isSelected: boolean) => {
        if (!isSelected) return;

        const html = document.getElementsByTagName('html')[0];

        if (currentTarget === DARK) {
            html.className = 'pf-v6-theme-glass pf-v6-theme-dark';
            setTheme(DARK);
            localStorage.setItem('theme', DARK);
        } else {
            html.className = 'pf-v6-theme-glass';
            setTheme(LIGHT);
            localStorage.setItem('theme', LIGHT);
        }
    });

    const headerToolbar = (
        <Toolbar id="vertical-toolbar">
            <ToolbarContent>
                <ToolbarGroup align={{ default: "alignEnd" }}>
                    <ToolbarItem>
                        <ToggleGroup>
                            <ToggleGroupItem
                                icon={<SunIcon />}
                                aria-label="Light theme"
                                buttonId={LIGHT}
                                isSelected={theme === LIGHT}
                                onChange={(event, isSelected: boolean) => toggleDarkMode(event.currentTarget.id, isSelected)}
                            />
                            <ToggleGroupItem
                                icon={<MoonIcon />}
                                aria-label="Dark theme"
                                buttonId={DARK}
                                isSelected={theme === DARK}
                                onChange={(event, isSelected: boolean) => toggleDarkMode(event.currentTarget.id, isSelected)}
                            />
                        </ToggleGroup>
                    </ToolbarItem>
                    <ToolbarItem>
                        <Button
                            variant='link'
                            href="https://github.com/redawl/Workout-Tracker"
                            icon={<GithubIcon />}
                            component='a'
                        />
                    </ToolbarItem>
                    <ToolbarItem>
                        <Button variant='secondary' onClick={() => logOut()}>
                            Sign out
                        </Button>
                    </ToolbarItem>
                </ToolbarGroup>
            </ToolbarContent>
        </Toolbar>
    );

    const header = (
        <Masthead>
            <MastheadMain>
                <MastheadToggle>
                    <PageToggleButton
                        isHamburgerButton
                        variant="plain"
                        isSidebarOpen={isSidebarOpen}
                        onSidebarToggle={onSidebarToggle}
                    />
                </MastheadToggle>
                <MastheadBrand>
                    <MastheadLogo href="/" target="_blank">
                        <svg viewBox="0 0 679 158">
                            <title>Workout Tracker</title>
                            <image href='/icon.png' height='100%' />
                        </svg>
                    </MastheadLogo>
                </MastheadBrand>
            </MastheadMain>
            <MastheadContent>{headerToolbar}</MastheadContent>
        </Masthead>
    );

    const sidebar = (
        <PageSidebar isSidebarOpen={isSidebarOpen}>
            <PageSidebarBody>
                <Nav
                    onSelect={(_, result) => setCurrentPage(result.itemId)}
                >
                    <NavItem
                        preventDefault
                        itemId={WORKOUTS}
                        isActive={currentPage === WORKOUTS}
                    >
                        {WORKOUTS}
                    </NavItem>
                    <NavItem
                        preventDefault
                        itemId={EXERCISES}
                        isActive={currentPage === EXERCISES}
                    >
                        {EXERCISES}
                    </NavItem>
                    <NavItem
                        preventDefault
                        itemId={REPORTS}
                        isActive={currentPage === REPORTS}
                    >
                        {REPORTS}
                    </NavItem>
                    <NavItem
                        preventDefault
                        itemId={DOCUMENTATION}
                        isActive={currentPage === DOCUMENTATION}
                    >
                        {DOCUMENTATION}
                    </NavItem>
                </Nav>
            </PageSidebarBody>
        </PageSidebar>
    );

    return (
        <Page masthead={header} sidebar={sidebar}>
            <PageSection>
                {
                    currentPage === WORKOUTS ? (
                        <Workouts />
                    ) : currentPage === EXERCISES ? (
                        <Exercises />
                    ) : currentPage === REPORTS ? (
                        <Reports />
                    ) : (
                        <Documentation />
                    )
                }
            </PageSection>
        </Page>
    );
};
