import { test, expect } from '@playwright/test';

const casos = [
  {
    cep: '80000000',
    valor: '100',
    resultado: 'Frete: R$ 15,00',
    valido: true,
    classe: 'CEP iniciado em 8'
  },
  {
    cep: '10000000',
    valor: '100',
    resultado: 'Frete: R$ 25,00',
    valido: true,
    classe: 'CEP não iniciado em 8'
  },
  {
    cep: '80000000',
    valor: '200',
    resultado: 'Frete grátis',
    valido: true,
    classe: 'limite para frete grátis'
  },
  {
    cep: '80000000',
    valor: '200,01',
    resultado: 'Frete grátis',
    valido: true,
    classe: 'acima do limite para frete grátis'
  },
  {
    cep: '1234567',
    valor: '100',
    resultado: 'Dados inválidos',
    valido: false,
    classe: 'CEP com menos de 8 dígitos'
  },
  {
    cep: '123456789',
    valor: '100',
    resultado: 'Dados inválidos',
    valido: false,
    classe: 'CEP com mais de 8 dígitos'
  },
  {
    cep: '3bcdefgh',
    valor: '100',
    resultado: 'Dados inválidos',
    valido: false,
    classe: 'CEP com letras'
  },
  {
    cep: '80000000',
    valor: '0',
    resultado: 'Dados inválidos',
    valido: false,
    classe: 'valor igual a zero'
  },
  {
    cep: '80000000',
    valor: '-10',
    resultado: 'Dados inválidos',
    valido: false,
    classe: 'valor negativo'
  },
  {
    cep: '80000000',
    valor: 'abc',
    resultado: 'Dados inválidos',
    valido: false,
    classe: 'valor inválido'
  },
  {
    cep: '',
    valor: '',
    resultado: 'Dados inválidos',
    valido: false,
    classe: 'campos vazios'
  },
  {
    cep: '',
    valor: '100',
    resultado: 'Dados inválidos',
    valido: false,
    classe: 'cep vazio'
  }
];

for (const caso of casos) {

  test(
    `frete ${caso.cep || '(vazio)'} - ${caso.valor || '(vazio)'} - ${caso.classe}`,
    async ({ page }) => {

      await page.goto('/frete');

      await page.getByLabel('CEP').fill(caso.cep);

      await page
        .getByLabel('Valor do pedido')
        .fill(caso.valor);

      await page
        .getByRole('button', { name: 'Calcular frete' })
        .click();

      const resultado = page.locator('#resultado');

      await expect(resultado).toBeVisible();

      await expect(resultado).toHaveText(caso.resultado);

      await expect(resultado).toHaveAttribute(
        'role',
        caso.valido ? 'status' : 'alert'
      );

    }
  );

}