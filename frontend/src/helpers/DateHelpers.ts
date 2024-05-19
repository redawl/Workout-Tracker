export const formatDate = (date: Date) =>{
    return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}`;
}

export const pad = (dateComponent: number) => {
    return dateComponent.toString().length == 1 ? `0${dateComponent.toString()}`: dateComponent.toString()
}