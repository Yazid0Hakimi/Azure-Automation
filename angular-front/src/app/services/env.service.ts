import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class EnvService {
    get production(): boolean {
        return (window as any).__env?.production === 'true';
    }

    get apiGateway(): string {
        return (window as any).__env?.apiGateway || '';
    }

    get profileServiceEndpoint(): string {
        return (window as any).__env?.profileServiceEndpoint || '';
    }

    get performanceServiceEndpoint(): string {
        return (window as any).__env?.performanceServiceEndpoint || '';
    }
}