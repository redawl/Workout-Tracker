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
    ToolbarGroup
} from '@patternfly/react-core';
import { GithubIcon, BarsIcon, SunIcon, MoonIcon } from '@patternfly/react-icons';
import { Workouts } from './Workouts';
import { Exercises } from './Exercises';
import { Reports } from './Reports';
import { WORKOUTS, EXERCISES, REPORTS, DARK, LIGHT } from './constants';

export const Navigation: React.FunctionComponent = () => {
    const [ isSidebarOpen, setIsSidebarOpen ] = React.useState(true);
    const [ currentPage, setCurrentPage ] = React.useState(WORKOUTS);
    const [ theme, setTheme ] = React.useState('dark' as PageSidebarProps["theme"]);

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
                <ToolbarItem>Workout Tracker</ToolbarItem>
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
                            href="https://github.com/redawl/Workout-Tracker-Frontend"
                            icon={<GithubIcon />}
                            component='a'
                        />
                    </ToolbarItem>
                </ToolbarGroup>
            </ToolbarContent>
        </Toolbar>
    );

    const header = (
        <Masthead>
            <MastheadToggle>
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
                    Logo
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
                        Workouts
                    </NavItem>
                    <NavItem
                        preventDefault
                        itemId={EXERCISES}
                        isActive={currentPage === EXERCISES}
                    >
                        Exercises
                    </NavItem>
                    <NavItem
                        preventDefault
                        itemId={REPORTS}
                        isActive={currentPage === REPORTS}
                    >
                        Reports
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
                        <Workouts />
                    ) : currentPage === EXERCISES ? (
                        <Exercises />
                    ) : (
                        <Reports />
                    )
                }
            </PageSection>
        </Page>
    );
};
