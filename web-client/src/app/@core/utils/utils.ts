export class Utils {
    public static copyToChipboard(content: string = '') {
        const selBox = document.createElement('textarea');
        selBox.style.position = 'fixed';
        selBox.style.left = '0';
        selBox.style.top = '0';
        selBox.style.opacity = '0';
        selBox.value = content;
        document.body.appendChild(selBox);
        selBox.focus();
        selBox.select();
        document.execCommand('copy', false, null);
        document.body.removeChild(selBox);
    }
}
