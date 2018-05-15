import {NbMenuItem} from '@nebular/theme';

export const MENU_ITEMS: NbMenuItem[] = [
    {
        title: 'Домашняя страница',
        icon: 'nb-home',
        link: '/pages/dashboard',
        home: true,
    },
    {
        title: 'Приложения',
        group: true,
    },
    {
        title: 'Парольница',
        icon: 'nb-locked',
        children: [
            {
                title: 'Главная',
                link: 'app/kolpass',
            }]
    }
];
