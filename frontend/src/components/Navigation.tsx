import React from 'react';
import {
    Page,
    Masthead,
    MastheadToggle,
    MastheadMain,
    MastheadBrand,
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
    PageSidebarProps,
    ToolbarGroup,
    Brand,
    Title,
    TextVariants
} from '@patternfly/react-core';
import { GithubIcon, BarsIcon, SunIcon, MoonIcon } from '@patternfly/react-icons';
import { Workouts } from '../pages/Workouts';
import { Exercises } from '../pages/Exercises';
import { Reports } from '../pages/Reports';
import { WORKOUTS, EXERCISES, REPORTS, DARK, LIGHT, DOCUMENTATION } from './constants';
import { Documentation } from '../pages/Documentation';
import Session from "supertokens-auth-react/recipe/session";
import { signOut } from "supertokens-auth-react/recipe/emailpassword";

export const Navigation = () => {
    const [ isSidebarOpen, setIsSidebarOpen ] = React.useState(true);
    const [ currentPage, setCurrentPage ] = React.useState(WORKOUTS);
    const [ theme, setTheme ] = React.useState('dark' as PageSidebarProps["theme"]);
    const [accessToken, setAccessToken] = React.useState('');

    async function logOut(){
        await signOut();
        window.location.href = '/auth';
    }

    React.useEffect(() => {
        Session.getAccessToken().then((token) => {
            if(token !== undefined){
                setAccessToken(token);
            }
        })
    }, []);

    React.useEffect(() => {
        const savedTheme = localStorage.getItem('theme') as PageSidebarProps["theme"];
        const html = document.getElementsByTagName('html')[0];

        if(savedTheme === DARK){
            html.className = 'pf-v5-theme-dark';
            setTheme(DARK);
            localStorage.setItem('theme', DARK);
        } else {
            html.className = '';
            setTheme(LIGHT);
            localStorage.setItem('theme', LIGHT);
        }
    }, [theme])

    const onSidebarToggle = () => {
        setIsSidebarOpen(!isSidebarOpen);
    };

    const toggleDarkMode = ((currentTarget: string, isSelected: boolean) => {
        if(!isSelected) return;

        const html = document.getElementsByTagName('html')[0];

        if(currentTarget === DARK){
            html.className = 'pf-v5-theme-dark';
            setTheme(DARK);
            localStorage.setItem('theme', DARK);
        } else {
            html.className = '';
            setTheme(LIGHT);
            localStorage.setItem('theme', LIGHT);
        }
    });

    const headerToolbar = (
        <Toolbar id="vertical-toolbar">
            <ToolbarContent>
                <ToolbarItem>
                    <Title headingLevel={TextVariants.h1} >
                        Workout Tracker
                    </Title>
                </ToolbarItem>
                <ToolbarGroup align={{ default: "alignRight" }}>
                    <ToolbarItem>
                        <ToggleGroup>
                            <ToggleGroupItem 
                                icon={<SunIcon/>}
                                aria-label="Light theme"
                                buttonId={ LIGHT }
                                isSelected= { theme === LIGHT }
                                onChange={(event, isSelected: boolean) => toggleDarkMode(event.currentTarget.id, isSelected)}
                            />
                            <ToggleGroupItem 
                                icon={<MoonIcon/>}
                                aria-label="Dark theme"
                                buttonId={ DARK }
                                isSelected={ theme === DARK }
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
            <MastheadToggle width='100px'>
                <PageToggleButton
                    variant="plain"
                    aria-label="Global navigation"
                    isSidebarOpen={isSidebarOpen}
                    onSidebarToggle={onSidebarToggle}
                    id="vertical-nav-toggle"
                >
                    <BarsIcon />
                </PageToggleButton>
            </MastheadToggle>
            <MastheadMain>
                <MastheadBrand href="/" target="_blank">
                    <Brand alt='icon' src='/icon.png' widths={{ default: '100px'}}/>
                </MastheadBrand>
            </MastheadMain>
            <MastheadContent>{headerToolbar}</MastheadContent>
        </Masthead>
    );

    const sidebar = (
        <PageSidebar theme={ theme } isSidebarOpen={isSidebarOpen} id="vertical-sidebar">
            <PageSidebarBody>
                <Nav theme={ theme } onSelect={(_event: React.FormEvent<HTMLInputElement>, result: { itemId: number | string }) => { setCurrentPage(result.itemId as string); }} aria-label="Grouped global">
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
        <Page header={header} sidebar={sidebar}>
            <PageSection>
                {
                    currentPage === WORKOUTS ? (
                        <Workouts accessToken={ accessToken }/>
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
