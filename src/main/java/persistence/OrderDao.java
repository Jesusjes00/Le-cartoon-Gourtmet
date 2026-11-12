package persistence;

import java.util.List;
import java.util.Map;

import Restaurante_Completo.LeCartoon_Original.Order;
import Restaurante_Completo.LeCartoon_Original.OrderDetails;
import Restaurante_Completo.LeCartoon_Original.Repartidor;
import Restaurante_Completo.LeCartoon_Original.Resena;
import Restaurante_Completo.LeCartoon_Original.Carrito.ItemPedido;

public interface OrderDao {
	// pedidos del cliente ; lista vacia si no tiene nada
	List<Order> pedidosDe(int idCliente);
	//renglones del pedido
	List<OrderDetails>detallesDe(int idPedido);
	//Cambiar el estado
	void cambiarEstado(int idPedido,int estado);
	//Insertar pedido mas los detalles en una sola transaccion ;regresa el id del pedido
	//idDireccion: a dónde se entrega (PEDIDO.ID_DIRECCION)
	int registrar(int idCliente, int idDireccion, double total, List<ItemPedido> items);
	// reseña del pedido; null si todavía no lo califican
	Resena resenaDe(int idPedido);
	// guarda la reseña de un pedido entregado (solo una vez)
	void calificar(int idPedido, Resena resena);
	
	// sucursal asignada al pedido (la usa el documento de Couchbase)
	int sucursalDePedido(int idPedido);

	// a los pedidos del cliente en Enviado/Entregado sin repartidor les asigna uno de su sucursal;
	// regresa id_pedido -> repartidor asignado (vacío si no hubo)
	Map<Integer, Repartidor> asignarRepartidores(int idCliente);

}
