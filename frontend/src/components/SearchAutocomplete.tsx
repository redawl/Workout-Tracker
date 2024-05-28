import React from 'react';
import { Menu, MenuContent, MenuItem, MenuList, Popper, SearchInput } from '@patternfly/react-core';

import { Exercise } from '../workout-api-client/types.gen.js';

export const SearchAutocomplete = ({ exercises, index, startingValue, parentOnSelect, parentOnChange }:
    {
        exercises: Array<Exercise>, 
        index: number, 
        startingValue: string, 
        parentOnSelect: (exercise: Exercise, index: number) => void,
        parentOnChange: (exerciseIndex: number, exerciseName: string) => void
    }) => {
    const [value, setValue] = React.useState(startingValue);
    const [hint, setHint] = React.useState('');
    const [autocompleteOptions, setAutocompleteOptions] = React.useState([] as React.JSX.Element[]);

    const [isAutocompleteOpen, setIsAutocompleteOpen] = React.useState(false);

    const searchInputRef = React.useRef<HTMLInputElement>(null);
    const autocompleteRef = React.useRef<HTMLInputElement>(null);

    React.useEffect(() => {
        parentOnChange(index, value);
    }, [value, index, parentOnChange])

    const onClear = () => {
        setValue('');
    };

    const onChange = (_event: React.FormEvent<HTMLInputElement>, newValue: string) => {
        if (
            newValue !== '' &&
            searchInputRef &&
            searchInputRef.current &&
            searchInputRef.current.contains(document.activeElement)
        ) {
            setIsAutocompleteOpen(true);

            // When the value of the search input changes, build a list of no more than 10 autocomplete options.
            // Options which start with the search input value are listed first, followed by options which contain
            // the search input value.
            let options = exercises
                .filter((option) => option.name.toLowerCase().startsWith(newValue.toLowerCase()))
                .map((option, index) => (
                    <MenuItem itemId={option.name} key={index}>
                        {option.name}
                    </MenuItem>
                ));
            if (options.length > 10) {
                options = options.slice(0, 10);
            } else {
                options = [
                    ...options,
                    ...exercises
                        .filter((option) => !option.name.startsWith(newValue.toLowerCase()) && option.name.includes(newValue.toLowerCase()))
                        .map((option, index) => (
                            <MenuItem itemId={option} key={index}>
                                {option.name}
                            </MenuItem>
                        ))
                ].slice(0, 10);
            }

            // The hint is set whenever there is only one autocomplete option left.
            setHint(options.length === 1 ? options[0].props.itemId : '');
            // The menu is hidden if there are no options
            setIsAutocompleteOpen(options.length > 0);
            setAutocompleteOptions(options);
        } else {
            setIsAutocompleteOpen(false);
        }
        setValue(newValue);
    };

    // Whenever an autocomplete option is selected, set the search input value, close the menu, and put the browser
    // focus back on the search input
    const onSelect = (e: React.MouseEvent<Element, MouseEvent> | undefined, itemId: string | number | undefined) => {
        e && e.stopPropagation();
        setValue(itemId as string);
        setIsAutocompleteOpen(false);
        console.log(exercises);
        console.log(value);
        parentOnSelect(exercises.filter(exercise => exercise.name.toLowerCase() === (itemId as string).toLowerCase())[0], index);
        searchInputRef.current && searchInputRef.current.focus();
    };

    const handleMenuKeys = (event: KeyboardEvent) => {
        // If there is a hint while the browser focus is on the search input, tab or right arrow will 'accept' the hint value
        // and set it as the search input value
        if (hint && (event.key === 'Tab' || event.key === 'ArrowRight') && searchInputRef.current === event.target) {
            setValue(hint);
            setHint('');
            setIsAutocompleteOpen(false);
            if (event.key === 'ArrowRight') {
                event.preventDefault();
            }
            // if the autocomplete is open and the browser focus is on the search input,
        } else if (isAutocompleteOpen && searchInputRef.current && searchInputRef.current === event.target) {
            // the escape key closes the autocomplete menu and keeps the focus on the search input.
            if (event.key === 'Escape') {
                setIsAutocompleteOpen(false);
                searchInputRef.current.focus();
                // the up and down arrow keys move browser focus into the autocomplete menu
            } else if (autocompleteRef.current && (event.key === 'ArrowDown' || event.key === 'ArrowUp')) {
                const firstElement: HTMLInputElement | null = autocompleteRef.current.querySelector('li > button:not(:disabled)');
                firstElement && firstElement.focus();
                event.preventDefault(); // by default, the up and down arrow keys scroll the window
                // the tab, enter, and space keys will close the menu, and the tab key will move browser
                // focus forward one element (by default)
            } else if (event.key === 'Tab' || event.key === 'Enter' || event.key === 'Space') {
                setIsAutocompleteOpen(false);
                if (event.key === 'Enter' || event.key === 'Space') {
                    event.preventDefault();
                }
            }
            // If the autocomplete is open and the browser focus is in the autocomplete menu
            // hitting tab will close the autocomplete and but browser focus back on the search input.
        } else if (isAutocompleteOpen && autocompleteRef.current && autocompleteRef.current.contains(event.target as Node) && event.key === 'Tab') {
            event.preventDefault();
            setIsAutocompleteOpen(false);
            searchInputRef.current && searchInputRef.current.focus();
        }
    };

    // The autocomplete menu should close if the user clicks outside the menu.
    const handleClickOutside = (event: MouseEvent) => {
        if (
            isAutocompleteOpen &&
            autocompleteRef &&
            autocompleteRef.current &&
            !autocompleteRef.current.contains(event.target as Node)
        ) {
            setIsAutocompleteOpen(false);
        }
    };

    React.useEffect(() => {
        window.addEventListener('keydown', handleMenuKeys);
        window.addEventListener('click', handleClickOutside);
        return () => {
            window.removeEventListener('keydown', handleMenuKeys);
            window.removeEventListener('click', handleClickOutside);
        };
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [isAutocompleteOpen, hint, searchInputRef.current]);

    const searchInput = (
        <SearchInput
            value={value}
            onChange={onChange}
            onClear={onClear}
            ref={searchInputRef}
            hint={hint}
            id="autocomplete-search"
        />
    );

    const autocomplete = (
        <Menu ref={autocompleteRef} onSelect={onSelect}>
            <MenuContent>
                <MenuList>{autocompleteOptions}</MenuList>
            </MenuContent>
        </Menu>
    );

    return (
        <Popper
            trigger={searchInput}
            triggerRef={searchInputRef}
            popper={autocomplete}
            popperRef={autocompleteRef}
            isVisible={isAutocompleteOpen}
            enableFlip={false}
            // append the autocomplete menu to the search input in the DOM for the sake of the keyboard navigation experience
            appendTo={() => document.querySelector('#autocomplete-search') as HTMLElement}
        />
    );
};
